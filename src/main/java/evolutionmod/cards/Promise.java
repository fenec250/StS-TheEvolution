package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.RefillDrawPileAction;
import evolutionmod.actions.TriggerScryEffectsAction;
import evolutionmod.orbs.LavafolkGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.FatePower;
import evolutionmod.powers.LustPower;

public class Promise
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Promise";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/LymeanSkl.png";
    private static final int COST = 1;
    private static final int FATE_AMT = 2;
    private static final int UPGRADE_FATE_AMT = 2;
    private static final int LYMEAN_LUST_AMT = 2;

    public Promise() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.ENEMY);
        this.magicNumber = this.baseMagicNumber = FATE_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int lust = formEffect(LymeanGene.ID) ? LYMEAN_LUST_AMT : 0;
        addToBot(new ApplyPowerAction(p, p, new FatePower(p, this.magicNumber), this.magicNumber, true));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                if (p.hasPower(FatePower.POWER_ID)) {
                    int fate = p.getPower(FatePower.POWER_ID).amount;
                    addToTop(new ReducePowerAction(p, p, FatePower.POWER_ID, fate));
                    addToTop(new PromiseAction(m, fate, lust));
                    addToTop(new RefillDrawPileAction(fate)); // addToTop last to run first
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FATE_AMT);
//            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(LymeanGene.ID)) {
            this.glowColor = LymeanGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    public static class PromiseAction extends AbstractGameAction {

        private static final UIStrings uiStrings;
        public static final String[] TEXT;
        static {
            uiStrings = CardCrawlGame.languagePack.getUIString("ReprogramAction");
            TEXT = uiStrings.TEXT;
        }

        private float startingDuration;
        private CardGroup fateGroup;
        private int baseLust;

        public PromiseAction(AbstractCreature target, int fateAmount, int baseLust) {
            this.amount = fateAmount;
            this.baseLust = baseLust;
            this.target = target;

            this.actionType = ActionType.CARD_MANIPULATION;
            this.startingDuration = Settings.ACTION_DUR_FAST;
            this.duration = this.startingDuration;
        }

        public void update() {
            // copied from ScryAction
            if (AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.isDone = true;
            } else {
//			Iterator var1;
                if (this.duration == this.startingDuration) {
                    addToTop(new TriggerScryEffectsAction());

                    if (AbstractDungeon.player.drawPile.isEmpty()
                            || amount <= 0) {
                        this.isDone = true;
                        return;
                    }
                    fateGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for(int i = 0; i < Math.min(this.amount, AbstractDungeon.player.drawPile.size()); ++i) {
                        fateGroup.addToTop(AbstractDungeon.player.drawPile.group.get(AbstractDungeon.player.drawPile.size() - i - 1));
                    }
                    if (fateGroup.isEmpty()) {
                        this.isDone = true;
                        return;
                    }

                    AbstractDungeon.gridSelectScreen.open(fateGroup, fateGroup.size(), true, TEXT[0]);
                } else {
                    if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
                        AbstractDungeon.gridSelectScreen.selectedCards.forEach(c -> {
                            AbstractDungeon.player.drawPile.removeCard(c);
                            AbstractDungeon.player.drawPile.moveToDiscardPile(c);
                        });
                        addToTop(new ApplyPowerAction(target, AbstractDungeon.player,
                                new LustPower(target, this.baseLust + AbstractDungeon.gridSelectScreen.selectedCards.size())));
                        AbstractDungeon.gridSelectScreen.selectedCards.clear();
                    }
//				fateGroup.shuffle();
//				List<AbstractCard> copy = new ArrayList<>(fateGroup.group);
//				copy.forEach(c -> fateGroup.moveToDeck(c, false));
                }

                this.tickDuration();
            }
        }
    }
}