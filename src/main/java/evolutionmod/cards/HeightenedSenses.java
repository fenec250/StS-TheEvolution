package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.RagePower;
import evolutionmod.orbs.BeastGene;
import evolutionmod.patches.AbstractCardEnum;

import java.util.Iterator;

public class HeightenedSenses
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:HeightenedSenses";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/HeightenedSenses.png";
    private static final int COST = 0;
    private static final int DRAW_AMT = 2;
    private static final int UPGRADED_DRAW_AMT = 1;
    private static final int FORM_DRAW_AMOUNT = 1;

    public HeightenedSenses() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = DRAW_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int draw = this.magicNumber;
        if (isPlayerInTheseForms(BeastGene.ID)) {
            draw += FORM_DRAW_AMOUNT;
        }

        int finalDraw = draw;
        addToBot(new DrawCardAction(finalDraw, new HeightenedSensesAction(p)));

        formEffect(BeastGene.ID);
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADED_DRAW_AMT);
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (isPlayerInThisForm(BeastGene.ID)) {
            this.glowColor = BeastGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    public static class HeightenedSensesAction extends AbstractGameAction {
        public HeightenedSensesAction(AbstractPlayer player) {
            this.source = player;
            this.target = player;
            this.duration = 0.001F;
        }

        public void update() {
            AbstractDungeon.actionManager.addToTop(new WaitAction(0.4F));
            this.tickDuration();
            if (this.isDone) {
                Iterator var1 = DrawCardAction.drawnCards.iterator();

//                int rage = 0;
                while(var1.hasNext()) {
                    AbstractCard c = (AbstractCard)var1.next();
                    if (c.type != AbstractCard.CardType.ATTACK) {
//                        rage += 1;
                        AbstractDungeon.player.hand.moveToDiscardPile(c);
                        c.triggerOnManualDiscard();
                        GameActionManager.incrementDiscard(false);
                    }
                }
//                if (rage > 0) {
//                	addToBot(new ApplyPowerAction(this.target, this.source, new RagePower(this.target, rage)));
//				}
            }

        }
    }
}