package evolutionmod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import evolutionmod.actions.FateAction;
import evolutionmod.actions.StrenghtenAction;
import evolutionmod.orbs.AbstractGene;
import evolutionmod.orbs.CentaurGene;
import evolutionmod.orbs.LymeanGene;
import evolutionmod.patches.AbstractCardEnum;
import evolutionmod.powers.StrenghtenPower;

public class Strenghten
        extends BaseEvoCard {
    public static final String ID = "evolutionmod:Strenghten";
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = "evolutionmod/images/cards/CentaurSkl.png";
    private static final int COST = 1;
    private static final int VIGOR_AMT = 3;
    private static final int UPGRADE_VIGOR_AMT = 2;

    public Strenghten() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, AbstractCardEnum.EVOLUTION_BLUE,
                CardRarity.UNCOMMON, CardTarget.SELF);
        this.magicNumber = this.baseMagicNumber = VIGOR_AMT;
    }

        @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new ApplyPowerAction(p, p,
                new VigorPower(p, this.magicNumber)));

        if (!AbstractGene.isPlayerInThisForm(CentaurGene.ID)) {
            addToBot(new ChannelAction(new CentaurGene()));
        } else {
            if (!AbstractGene.isPlayerInThisForm(LymeanGene.ID)) {
                addToBot(new ChannelAction(new LymeanGene()));
            } else {
                addToBot(new StrenghtenAction(p));
//                this.exhaust = true;
            }
        }
    }

    @Override
    public AbstractCard makeCopy() {
        return new Strenghten();
    }

    @Override
    public void upgrade() {
        if (!this.upgraded) {
            this.upgradeName();
            this.upgradeMagicNumber(UPGRADE_VIGOR_AMT);
        }
    }
}
