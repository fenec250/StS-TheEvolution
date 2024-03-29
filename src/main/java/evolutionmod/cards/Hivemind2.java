package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.orbs.InsectGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.FatePower;

public class Hivemind2
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Hivemind";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Hivemind.png";
    private static final int COST = 0;
    private static final int FATE_PER_DRONE = 1;
    private static final int UPGRADE_FATE_PER_DRONE = 1;

    public Hivemind2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.cardsToPreview = new Drone();
        this.baseMagicNumber = this.magicNumber = FATE_PER_DRONE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new InsectGene().getChannelAction());
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                int count = magicNumber * (int) p.hand.group.stream().filter(card -> Drone.ID.equals(card.cardID)).count();
                if (count > 0) {
                    addToTop(new ApplyPowerAction(p, p, new FatePower(p, count), count));
                }
                this.isDone = true;
            }
        });
    }

    @Override
    public AbstractCard makeCopy() {
        return new Hivemind2();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_FATE_PER_DRONE);
//            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
//
//    @Override
//    public void triggerOnGlowCheck() {
//        if (isPlayerInThisForm(InsectGene.ID)) {
//            this.glowColor = InsectGene.COLOR.cpy();
//        } else {
//            this.glowColor = BLUE_BORDER_GLOW_COLOR;
//        }
//    }
}