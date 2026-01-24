package evolutionmod.cardsV1;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import evolutionmod.actions.StrengthenAction;
import evolutionmod.cards.BaseEvoCard;
import evolutionmod.orbsV1.LymeanGene;
import evolutionmod.patches.EvolutionEnum;

public class Strengthen2
        extends BaseEvoCard {
    public static final String cardID = "Strengthen";
    public static final String ID = "evolutionmod:"+cardID;
	public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings("evolutionmod:"+cardID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Strengthen.png";
    private static final int COST = 1;
    private static final int FATE_ATTACK_AMT = 2;
    private static final int LYMEAN_FATE_AMT = 1;

    public Strengthen2() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, EvolutionEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = FATE_ATTACK_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int fate = this.magicNumber;
        if (this.upgraded && BaseEvoCard.formEffect(LymeanGene.ID)) {
            fate += LYMEAN_FATE_AMT;
        }
        addToBot(new StrengthenAction(fate, p));
//        addToBot(new ApplyPowerAction(p, p,
//                new VigorPower(p, this.magicNumber)));
//        if (!upgraded) {
//            formEffect(LymeanGene.ID, () -> addToBot(new ChannelAction(new CentaurGene())));
//        } else {
//            addToBot(new ChannelAction(new CentaurGene()));
//            formEffect(LymeanGene.ID, () -> addToBot(new ApplyPowerAction(p, p, new VigorPower(p, this.magicNumber))));
//        }
//        formEffect(CentaurGene.ID, () -> formEffect(LymeanGene.ID, () -> {
//                addToBot(new StrenghtenAction(p));
//                this.exhaust = true;
//                p.orbs.stream()
//                        .filter(o -> this.canAdaptWith(o) > 0)
//                        .findAny()
//                        .ifPresent(o -> this.tryAdaptingWith(o, true));
//            }));
    }

    @Override
    public AbstractCard makeCopy() {
        return new Strengthen2();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
//            this.upgradeMagicNumber(UPGRADE_VIGOR_AMT);
            this.rawDescription = UPGRADE_DESCRIPTION;
            this.initializeDescription();
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        if (this.upgraded && isPlayerInThisForm(LymeanGene.ID)) {
            this.glowColor = LymeanGene.COLOR.cpy();
        } else {
            this.glowColor = BLUE_BORDER_GLOW_COLOR;
        }
    }
}
