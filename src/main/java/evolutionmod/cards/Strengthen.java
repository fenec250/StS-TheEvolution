package evolutionmod.cards;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;

public class Strengthen
        extends AdaptableEvoCard implements GlowingCard {
    public static final String ID = "evolutionmod:Strengthen";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/Strengthen.png";
    private static final int COST = 1;
    private static final int VIGOR_AMT = 3;

    public Strengthen() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = VIGOR_AMT;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
//        addToBot(new ApplyPowerAction(p, p,
//                new VigorPower(p, this.magicNumber)));
        if (!upgraded) {
            formEffect(LymeanGene.ID, () -> addToBot(new ChannelAction(new CentaurGene())));
        } else {
            addToBot(new ChannelAction(new CentaurGene()));
            formEffect(LymeanGene.ID, () -> addToBot(new ApplyPowerAction(p, p, new VigorPower(p, this.magicNumber))));
        }
        this.adapt(1);
//        formEffect(CentaurGene.ID, () -> formEffect(LymeanGene.ID, () -> {
//                addToBot(new StrenghtenAction(p));
//                this.exhaust = true;
//                p.orbs.stream()
//                        .filter(o -> this.canAdaptWith(o) > 0)
//                        .findAny()
//                        .ifPresent(o -> this.tryAdaptingWith(o, true));
//            }));
        this.useAdaptations(p, m);
    }

    @Override
    public int canAdaptWith(AbstractAdaptation adaptation) {
        return adaptation.getGeneId().equals(CentaurGene.ID) && adaptation.amount >= 1 ? 1 : 0;
    }

    @Override
    public AbstractCard makeCopy() {
        return new Strengthen();
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
    public int getNumberOfGlows() {
        return 2;
    }

    @Override
    public boolean isGlowing(int glowIndex) {
        return isPlayerInThisForm(LymeanGene.ID);
    }

    @Override
    public Color getGlowColor(int glowIndex) {
        return LymeanGene.COLOR.cpy();
    }
}
